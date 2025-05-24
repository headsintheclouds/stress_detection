package com.example.stress_detection.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.stress_detection.dto.AccountInfoDTO;
import com.example.stress_detection.dto.PasswordDTO;
import com.example.stress_detection.entity.User;
import com.example.stress_detection.exception.PasswordErrorException;
import com.example.stress_detection.mapper.UserMapper;
import com.example.stress_detection.result.Result;
import com.example.stress_detection.service.UserService;
import com.example.stress_detection.utils.JwtUtil;
import com.example.stress_detection.vo.AccountInfoVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "账号管理")
@RestController
@RequestMapping("/users")
public class AccountController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    @ApiOperation("修改个人信息")
    //TODO 前端加上验证功能， 验证格式是否正确
    @Transactional
    public Result<AccountInfoVO> modifyInfo(@RequestBody AccountInfoDTO accountInfoDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("first");
        // 1.首先身份校验jwt
        // 1. 获取并验证 JWT
        String token = dealToken(request);
        if(token != null) {
            boolean jwtExpired = JwtUtil.isJWTExpired(token);
            if(jwtExpired) {
                // JWT过期了
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置状态码为 401
                response.getWriter().write("JWT is Expired");
                return null; // 不再继续执行后续代码
            } else {
                // JWT有效
                // 2.获取信息
                String username = accountInfoDTO.getUsername();
                String phone = accountInfoDTO.getPhone();
                String email = accountInfoDTO.getEmail();

                // 3.修改信息
                User user = userService.modifyUserInfo(accountInfoDTO);

                // 4.验证
                if(user != null && user.getName().equals(username) && user.getPhone().equals(phone) && user.getEmail().equals(email)) {
                    // 5.返回
                    AccountInfoVO accountInfoVO = AccountInfoVO.builder()
                            .userId(user.getUserId())
                            .userName(user.getName())
                            .phone(user.getPhone())
                            .email(user.getEmail())
                            .build();
                    return Result.success(accountInfoVO);
                } else {
                    return Result.fail("修改失败，信息未能正确更新！");
                }
            }
        }
        return Result.fail("修改失败，未提供有效的JWT！");
    }

    @PutMapping("/password")
    @ApiOperation("修改用户密码")
    @Transactional
    //TODO 待完善，密码加密，
    public Result changePassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1.首先身份校验jwt
        // 1. 获取并验证 JWT
        String token = dealToken(request);
        if(token == null) {
//            throw new PasswordErrorException("未登录或Token已过期");
            return Result.fail("未登录或Token已过期");
        }
        System.out.println("测试测试测试测试测试测试测试测试测试测试");
        boolean jwtExpired = JwtUtil.isJWTExpired(token);
        if(jwtExpired) {
            // JWT过期了
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置状态码为 401
//            response.getWriter().write("JWT is Expired");
            return Result.fail("JWT is Expired");
//            return null; // 不再继续执行后续代码
        }

        if (!userService.isPasswordCorrect(passwordDTO)) {
            System.out.println(" =----========================是这里的问题吗 " );
            return Result.fail("旧密码不正确");
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            return Result.fail("新密码两次输入不一致");
        }

//        if (passwordDTO.getNewPassword().length() < 8) {
//            return Result.fail("密码强度过低，请至少使用8位字符");
//        }

        System.out.println(passwordDTO.getUsername());

        Boolean res = userService.changePassword(passwordDTO);
        if(!res) {
            return Result.fail("修改失败！");
        }

        return Result.success();
    }

    @GetMapping("/account")
    @ApiOperation("查看个人账号信息")
    public Result<AccountInfoVO> getAccountInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 获取并验证 JWT
        String token = dealToken(request);

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未提供有效的JWT！");
            return null;
        }

        boolean jwtExpired = JwtUtil.isJWTExpired(token);
        if (jwtExpired) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT is Expired");
            return null;
        }
        System.out.println("测试 ，， 进去了吗1111111111111111111111111111111111111111111");


        try {
            // 2. 解析 JWT，获取用户名
            String username = JwtUtil.parseJWT(token).get("username", String.class);
            if (username == null || username.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT中未包含有效用户名！");
                return null;
            }

            // 3. 查询用户信息
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return Result.fail("获取用户信息失败，用户不存在！");
            }

            // 4. 构建 VO 并返回
            AccountInfoVO accountInfoVO = AccountInfoVO.builder()
                    .userId(user.getUserId())
                    .userName(user.getName())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();
            return Result.success(accountInfoVO);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT解析失败！");
            return null;
        }
    }

    String dealToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("token原始值: " + token);

        token = token.substring(7); // 移除 "Bearer "
        return token;
    }

}

