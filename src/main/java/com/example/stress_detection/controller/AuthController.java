package com.example.stress_detection.controller;


import com.example.stress_detection.dto.UserLoginDTO;
import com.example.stress_detection.entity.User;
import com.example.stress_detection.result.Result;
import com.example.stress_detection.service.UserService;
import com.example.stress_detection.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Api(tags = "authentication 接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @PutMapping("/login")
    @ApiOperation("员工登录")
    //TODO 待完善
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();


        return Result.success(userLoginVO);
    }


    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<UserLoginVO> logout() {

        return Result.success();
    }
}
