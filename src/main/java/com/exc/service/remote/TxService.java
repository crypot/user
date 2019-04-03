package com.exc.service.remote;

import com.exc.client.AuthorizedFeignClient;
import com.exc.domain.CurrencyName;
import com.exc.service.dto.GenWalletDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@AuthorizedFeignClient(name = "tx")
public interface TxService {
    @RequestMapping(value = "/api/internal/gen-wallets/{id}", method = RequestMethod.POST)
    List<GenWalletDTO> genWallets(@PathVariable("id") Long userId, @RequestBody List<CurrencyName> currencies);

}
