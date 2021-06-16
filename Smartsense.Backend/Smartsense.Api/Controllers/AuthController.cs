using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Web;
using Microsoft.AspNetCore.Http.Extensions;
using Smartsense.Business.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;

namespace Smartsense.Api.Controllers
{
    [Authorize]
    [Route("api/auth")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly IConfiguration _configuration;

        public AuthController(IUserService userService, IConfiguration configuration)
        {
            _userService = userService;
            _configuration = configuration;
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("register")]
        public UserRegisterResponse Register(UserRegisterRequest userRegisterRequest)
        {
            userRegisterRequest.Ip = HttpContext.Connection.RemoteIpAddress?.ToString();
            return _userService.Register(userRegisterRequest);
        }
        [AllowAnonymous]
        [HttpPost]
        [Route("login")]
        public UserLoginResponse Login(UserLoginRequest userLoginRequest)
        {
            UserLoginResponse userLoginResponse = _userService.Login(userLoginRequest);
            if (userLoginResponse.Code.Equals("200"))
            {
                userLoginResponse.Token = GetToken(userLoginRequest.isRemember, userLoginResponse.UserId);
               // userLoginResponse.UserId = 0;
            }

            return userLoginResponse;
        }

        [HttpGet]
        [Route("check")]
        public IActionResult Check()
        {
            return Ok();
        }
       

        [HttpPost]
        [Route("putinfo")]
        public UserPutInfoResponse PutInfo(UserPutInfoRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

           return _userService.PutInfo(request);
        }

        [HttpGet]
        [Route("getinfo")]
        public UserGetInfoResponse GetInfo()
        {
            var request = new UserGetInfoRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _userService.GetInfo(request);
        }
        


        [HttpPost]
        [Route("changepassword")]
        public ChangePasswordResponse ChangePassword(ChangePasswordRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _userService.ChangePassword(request);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("verify/{guid}")]
        public MailVerifyResponse Verify(string guid)
        {
            MailVerifyRequest request = new MailVerifyRequest();
            request.Guid = guid;
            return _userService.Verify(request);
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("PasswordForgot")]
        public PasswordForgotResponse PasswordForgot(PasswordForgotRequest request)
        {
            
            return _userService.PasswordForgot(request);
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("passwordreset")]
        public PasswordResetResponse PasswordReset(PasswordResetRequest request)
        {
            return _userService.PasswordReset(request);
        }
        [AllowAnonymous]
        [HttpPost]
        [Route("refreshtoken")]
        public RefreshTokenResponse RefreshToken(UserLoginRequest request)
        {
            RefreshTokenResponse response = new RefreshTokenResponse();

            UserLoginResponse userLoginResponse = _userService.Login(request);
            if (userLoginResponse.Code.Equals("200"))
            {
                response.Token = GetToken(true, userLoginResponse.UserId);
                userLoginResponse.UserId = 0;
            }

            if (userLoginResponse.Code.Equals("400"))
            {
                response.Code = "400";
                response.Errors.Add("Token yenilenemedi"); 
                return response;
            }

            response.Code = "200";
            response.Message = "Token yenilenmiştir";
            return response;
        }

        private string GetToken(bool isRemember, long id)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);
            var token = new JwtSecurityToken(
                _configuration["Jwt:Issuer"],
                _configuration["Jwt:Issuer"],
                (new[]
                {
                    new Claim("value", id.ToString())
                }),
                expires: isRemember ? DateTime.Now.AddMonths(3) : DateTime.Now.AddHours(24),
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}