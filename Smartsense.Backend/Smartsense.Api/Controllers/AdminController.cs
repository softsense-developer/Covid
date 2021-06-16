using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Smartsense.Business.Abstract;
using Smartsense.Entity.DTO;

namespace Smartsense.Api.Controllers
{
    [Authorize]
    [Route("api/admin")]
    [ApiController]
    public class AdminController : ControllerBase
    {
        private readonly IAdminService _adminService;

        public AdminController(IAdminService adminService)
        {
            _adminService = adminService;
        }

        [HttpPost]
        [Route("addhospital")]
        public AddHospitalResponse AddHospital(AddHospitalRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _adminService.AddHospital(request);
        }

        [HttpPost]
        [Route("supervisorpromotion")]
        public AdminPromotionResponse SupervisorPromotion(AdminPromotionRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _adminService.SupervisorPromotion(request);
        }

        [HttpGet]
        [Route("GetAllHospital")]
        public GetAllHospitalResponse GetAllHospital()
        {
            GetAllHospitalRequest request = new GetAllHospitalRequest();

            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _adminService.GetAllHospital(request);

        }

        [HttpGet]
        [Route("GetAllUsers")]
        public GetAllUsersResponse GetAllUsers()
        {
            GetAllUsersRequest request = new GetAllUsersRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _adminService.GetAllUsers(request);
        }
    }
}
