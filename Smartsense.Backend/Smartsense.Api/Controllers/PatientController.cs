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
    [Route("api/patient")]
    [ApiController]
    public class PatientController : ControllerBase
    {
        private readonly IPatientService _patientService;
        public PatientController(IPatientService patientService)
        {
           
            _patientService = patientService;
        }

        [HttpPost]
        [Route("addinfo")]
        public PatientAddInfoResponse AddInfo(PatientAddInfoRequest request)
        {
            
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _patientService.AddInfo(request);
        }


        [HttpGet]
        [Route("getinfo")]
        public PatientGetInfoResponse GetInfo()
        {
            var request = new PatientGetInfoRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _patientService.GetInfo(request);
        }

        [HttpPut]
        [Route("putinfo")]
        public PatientPutInfoResponse PutInfo(PatientPutInfoRequest request)
        {

            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _patientService.PutInfo(request);
        }
        
        [HttpGet]
        [Route(("getpatientlocation"))]
        public PatientGetLocationResponse GetPatientLocation()
        {
            PatientGetLocationRequest request = new PatientGetLocationRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _patientService.GetPatientLocation(request);
        }
        
        [HttpGet]
        [Route(("gethospital"))]
        public GetHospitalResponse GetHospital()
        {
            return _patientService.GetHospital();
        }
        
        [HttpGet]
        [Route(("getdoctor/{HospitalId}"))]
        public GetHospitalDoctorsResponse GetDoctors(long HospitalId)
        {
           return _patientService.GetDoctors(HospitalId);
        }

        [HttpPost]
        [Route("addquarantinelocation")]
        public LocationPostResponse AddQuarantineLocation(LocationPostRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _patientService.AddQuarantineLocation(request);
        }

        [HttpPut]
        [Route("putnowlocation")]
        public LocationPutResponse PutNowLocation(LocationPutRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _patientService.PutNowLocation(request);
        }

        [HttpGet]
        [Route("adddoctor/{DoktorId}")]
        public PatientRelationDoctorResponse AddDoctor(long DoktorId)
        {
            var request = new PatientRelationDoctorRequest();
            request.DoctorId = DoktorId;
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0)
                    .Value);
                request.UserId = id;
            }

            return _patientService.AddDoctor(request);
        }

        [HttpGet]
        [Route("quarantinestatus")]
        public PatientQuarantineStatusResponse QuarantineStatus()
        {
            var request = new PatientQuarantineStatusRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0)
                    .Value);
                request.UserId = id;
            }

           return _patientService.QuarantineStatus(request);

        }


        [HttpPost]
        [Route("addwarning")]
        public AddWarningResponse AddWarning(AddWarningRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0)
                    .Value);
                request.UserId = id;
            }

            return _patientService.AddWarning(request);
        }
        [HttpGet]
        [Route("getcompanions")]
        public GetCompanionResponse ListCompanion()
        {
            GetCompanionRequest request = new GetCompanionRequest();

            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0)
                    .Value);
                request.UserId = id;
            }

            return _patientService.ListCompanion(request);
        }

        [HttpPost]
        [Route("addcompanion")]
        public AddCompanionResponse AddCompanion(AddCompanionRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0)
                    .Value);
                request.UserId = id;
            }

            return _patientService.AddCompanion(request);
        }

        [HttpGet]
        [Route("deletecompanion/{CompanionId}")]
        public DeleteCompanionResponse AddCompanion(long CompanionId)
        {
            DeleteCompanionRequest request = new DeleteCompanionRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0)
                    .Value);
                request.UserId = id;
            }

            request.CompaionId = CompanionId;
            return _patientService.DeleteCompanion(request);
        }

        [HttpGet]
        [Route("getpromotions")]
        public GetPromotionResponse GetPromotion()
        {
            GetPromotionRequest request = new GetPromotionRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
            return _patientService.GetPromotion(request);
        }
        [HttpPost]
        [Route("connectionacceptrefuse")]
        public ConnectionAcceptRefuseResponse ConnectionAcceptRefuse(ConnectionAcceptRefuseRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _patientService.ConnectionAcceptRefuse(request);
        }
        [HttpPost]
        [Route("AudioTest")]
        public AudioTestResponse AudioTest(AudioTestRequest request)
        {
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }
           return _patientService.AudioTest(request);
        }

        }
}