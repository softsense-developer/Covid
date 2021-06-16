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
    [Route("api/supervisor")]
    [ApiController]
    public class SupervisorController : ControllerBase
    {
        private readonly ISupervisorService _supervisorService;
        public SupervisorController(ISupervisorService supervisorService)
        {
            _supervisorService = supervisorService;
        }


        [HttpGet]
        [Route("getdoctorsinfo")]
        public GetDoctorsResponse GetDoctorsInfo()
        {
            
            var request = new SupervisorGetDoctorsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.GetDoctorsInfo(request);


        }
        [HttpGet]
        [Route("getpatient/{PatientId}")]
        public GetPatientDataResponse GetPatientData(long PatientId)
        {
            var request = new GetPatientDataRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            request.PatientId = PatientId;

            return _supervisorService.GetPatientData(request);
        }


        [HttpGet]
        [Route("getsupervisorlocations")]
        public SupervisorGetLocationsResponse GetSupervisorLocations()
        {
            var request = new SupervisorGetLocationsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.GetSupervisorLocations(request);

        }

        [HttpPost]
        [Route("getpatienthistorydaterange")]
        public GetPatientDateRangeResponse GetPatientHistoryDateRange(GetPatientDateRangeRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }



            return _supervisorService.GetPatientHistoryDateRange(request);

        }
        [HttpPost]
        [Route("puthospital")]
        public PutHospitalResponse PutHospital(PutHospitalRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.PutHospital(request);
        }

        [HttpGet]
        [Route("getsupervisorhospital")]
        public GetSupervisorHospitalResponse GetSupervisorHospital()
        {
            var request = new GetSupervisorHospitalRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.GetHospital(request);
        }
        [HttpGet]
        [Route("gethospitaldoctors")]
        public GetHospitalDoctorsResponse GetHospitalDoctors()
        {
            var request = new GetHospitalDoctorsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.GetHospitalDoctors(request);
        }


        [HttpGet]
        [Route("doctorpromotion/{DoctorId}")]
        public SupervisorPromotionResponse DoctorPromotion(long DoctorId)
        {
            var request = new SupervisorPromotionRequest();
            request.DoctorId = DoctorId;
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.DoctorPromotion(request);
        }

        [HttpGet]
        [Route("doctordemotion/{DoctorId}")]
        public SupervisorDemotionResponse DoctorDemotion(long DoctorId)
        {
            var request = new SupervisorDemotionRequest();
            request.DoctorId = DoctorId;
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _supervisorService.DoctorDemotion(request);
        }


    }
}