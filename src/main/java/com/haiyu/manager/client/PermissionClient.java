package com.haiyu.manager.client;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author jason 2019.12.31
 */
//@FeignClient(value = "permission-client", url = "${permission.url}")
public interface PermissionClient {
    @GetMapping("/v1/authorities")
    List<Object> listAuthority();
}
