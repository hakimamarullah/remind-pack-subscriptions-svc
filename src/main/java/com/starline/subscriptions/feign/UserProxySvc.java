package com.starline.subscriptions.feign;

import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.proxy.UserInfo;
import com.starline.subscriptions.feign.config.FeignBasicConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${srv.feign.names.users:users-svc}",  configuration = FeignBasicConfig.class)
public interface UserProxySvc  {

    @GetMapping(value = "/users/info/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserInfo> getUserInfoById(@PathVariable Long id);

    @GetMapping(value = "/users/info/phone/{mobilePhone}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserInfo> getUserInfoByMobilePhone(@PathVariable String mobilePhone);
}
