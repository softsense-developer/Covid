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
    [Route("api/value")]
    [ApiController]
    public class ValueController : ControllerBase
    {
        private readonly IValueService _valueService;
        public ValueController(IValueService valueService)
        {
            _valueService = valueService;
        }

        [HttpPost]
        [Route("addvalue")]
        public ValueResponse AddValue(ValueRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _valueService.AddValue(request);
        }



        [HttpGet]
        [Route("gethistory")]
        public ValueHistoryResponse GetHistory()
        {
            var request = new ValueHistoryRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _valueService.GetHistory(request);
        }

        [HttpGet]
        [Route("getlastdata")]
        public ValueHistoryResponse GetLastData()
        {
            var request = new ValueHistoryRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _valueService.GetHistory(request);
        }
        [HttpPost]
        [Route("gethistorydaterange")]
        public GetDateRangeResponse GetHistoryDateRange(GetDateRangeRequest request)
        {
            
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _valueService.GetHistoryDateRange(request);
        }

    }
}