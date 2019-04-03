package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.domain.UserInfo;
import com.exc.domain.Wallet;
import com.exc.repository.UserInfoRepository;
import com.exc.repository.WalletRepository;
import com.exc.service.dto.BalanceStatusDTO;
import com.exc.service.dto.GenWalletDTO;
import com.exc.service.dto.WalletDTO;
import com.exc.service.mapper.WalletMapper;
import com.exc.service.remote.TxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing UserWallets.
 */
@Service
@Transactional
public class WalletService {
    private final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final UserInfoRepository userInfoRepository;
    private final TxService txService;

    public WalletService(WalletRepository walletRepository, WalletMapper walletMapper, UserInfoRepository userInfoRepository, TxService txService) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.userInfoRepository = userInfoRepository;
        this.txService = txService;
    }

    /**
     * create wallets, or add new wallets in case new currency were added in system
     *
     * @param userId
     * @param curName
     * @return
     */
    public List<WalletDTO> createWallets(Long userId, CurrencyName curName) {
        log.debug("requesting to gen wallets in thread: {}",
            Thread.currentThread().getName());

        List<WalletDTO> rs;
        UserInfo userInfo = userInfoRepository.findOneByUserId(userId).orElse(null);

        List<Wallet> existWalletList = walletRepository.findByUserInfo_Id(userInfo.getId());
        if (existWalletList.size() > 0 && existWalletList.size() != CurrencyName.values().length) {
            log.debug("Adding missed wallets to user {}", userInfo.getId());

            Map<CurrencyName, Wallet> curMapExisting = existWalletList.stream().collect(Collectors.toMap(wl -> wl.getCurrencyName(), wl -> wl));
            List<WalletDTO> newCurs =
                Arrays.stream(CurrencyName.values())
                    .filter(c -> curMapExisting.put(c, walletRepository.findByUserInfoAndCurrencyName(userInfo, c)) == null)
                    .map(c -> {
                        WalletDTO dto = new WalletDTO();
                        dto.setCurrencyName(c);
                        return dto;
                    }).collect(Collectors.toList());
            rs = genWallets(userInfo.getId(), newCurs.stream().map(w -> w.getCurrencyName()).collect(Collectors.toList()));

            List<Wallet> wallets = walletMapper.toEntity(rs);
            wallets.stream().forEach(w -> w.setUserInfo(userInfo));
            rs = walletMapper.toDto(walletRepository.saveAll(wallets));

        } else if (existWalletList.size() == 0) {
            log.debug("Request to gen new wallets to user {}", userInfo.getId());
            rs = genWallets(userId, Arrays.asList());
            existWalletList = walletMapper.toEntity(rs);
            existWalletList.forEach(wl -> {
                wl.setUserInfo(userInfo);
                wl.setId(null);
            });
            rs = walletMapper.toDto(walletRepository.saveAll(existWalletList));
        } else {
            log.debug("Request to get existing wallets from user {}", userInfo.getId());
            rs = walletMapper.toDto(existWalletList);
        }
        if (curName != null) {
            rs = rs.stream().filter(w -> w.getCurrencyName().equals(curName.name())).collect(Collectors.toList());
        }
        rs.stream().forEach(wlDTO -> wlDTO.setLockBalance(BigInteger.ZERO));
        return rs;
    }


    /**
     * generate wallets only, prevent creating records in DB.
     *
     * @param userInfoId
     * @param currencies
     * @return
     */
    private List<WalletDTO> genWallets(Long userInfoId, List<CurrencyName> currencies) {
        log.debug("Request to generate new wallets ");

        if (currencies == null || currencies.size() == 0) {
            currencies = Arrays.stream(CurrencyName.values()).collect(Collectors.toList());
        }

        List<GenWalletDTO> newWallets = txService.genWallets(userInfoId, currencies);
        log.debug("Generated {} new wallets ", newWallets.size());
        return walletMapper.toInternalDTOs(newWallets);
    }


    /**
     * Save a wallet.
     *
     * @param walletDTO the entity to save
     * @return the persisted entity
     */
    public WalletDTO save(WalletDTO walletDTO) {
        log.debug("Request to save UserWallets : {}", walletDTO.getId());
        Wallet wallet = walletMapper.toEntity(walletDTO);
        wallet = walletRepository.save(wallet);
        return walletMapper.toDto(wallet);
    }

    /**
     * Get all the wallets, related to the current user.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WalletDTO> findByUserLogin(Pageable pageable, Long userId) {
        Optional<UserInfo> userInfo = userInfoRepository.findOneByUserId(userId);
        if (!userInfo.isPresent()) {
            log.error("Can't find userInfo by Id : {}", userId);
        }
        log.debug("Request to get all Wallets of {}", userId);
        return walletRepository.findByUserInfo(userInfo.get(), pageable)
            .map(wal -> walletMapper.toDto(wal));
    }

    /**
     * Get one wallet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WalletDTO findOneByUser(Long id, Long userId) {
        log.debug("Request to get UserWallets : {}", id);
        Optional<UserInfo> userInfo = userInfoRepository.findOneByUserId(userId);
        if (!userInfo.isPresent()) {
            log.error("Can't find userInfo by login : {}", userId);
        }
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (!wallet.isPresent() || wallet.get().getUserInfo().getId() != userInfo.get().getId()) {
            log.error("Hack attempt detected...");
            wallet = null;
        }

        return walletMapper.toDto(wallet.orElse(null));
    }
/*
    private WalletDTO postInitWallet(WalletDTO walletDTO, Wallet wallet) {
        INode node = nodeAdapterFactory.getNode(walletDTO.getCurrencyName());
        walletDTO.setPublicKey(node.toPublic(wallet.getPrivateKey()));
        walletDTO.setBalance(node.getBalance(walletDTO.getPublicKey()));
        CurrencyFee fee = cryptoCurrencyRepository.findOne(walletDTO.getCurrencyId()).getFee();
        walletDTO.setMinAmount(wallet.getCurrency().getMinAmount());
        walletDTO.setFee(currencyFeeMapper.toDto(fee));
        walletDTO.setLockBalance(walletSyncService.getLockedBalance(walletDTO.getId(), walletDTO.getCurrencyId()));
        return walletDTO;

    }*/




    /**
     * Get all the wallets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WalletDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Wallets");
        return walletRepository.findAll(pageable)
            .map(walletMapper::toDto);
    }

    /**
     * Get one wallet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WalletDTO findOne(Long id) {
        log.debug("Request to get UserWallets : {}", id);
        Wallet wallet = walletRepository.findById(id).orElse(null);
        return walletMapper.toDto(wallet);
    }

    /**
     * Delete the wallet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserWallets : {}", id);
        walletRepository.deleteById(id);
    }


}
