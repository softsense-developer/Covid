using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Web;
using Microsoft.AspNetCore.Http.Extensions;
using Smartsense.Business.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;

namespace Smartsense.Api.Controllers
{
    [Authorize]
    [Route("api/doctor")]
    [ApiController]
    public class DoctorController : ControllerBase
    {
        private readonly IDoctorService _doctorService;

        public DoctorController(IDoctorService doctorService)
        {
            _doctorService = doctorService;
        }


        [HttpGet]
        [Route("getpatients")]
        public DoctorGetPatientsResponse GetPatients()
        {
            var request = new DoctorGetPatientsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _doctorService.GetInfo(request);


        }

        [HttpGet]
        [Route("getdoctorlocations")]
        public DoctorGetLocationsResponse GetDoctorLocations()
        {
            DoctorGetLocationsRequest request = new DoctorGetLocationsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _doctorService.GetDoctorLocations(request);
        }

        [HttpGet]
        [Route("getpatientdata/{patientid}")]
        public PatientValueResponse GetPatientData(long PatientId)
        {
            var request = new PatientValueRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            request.PatientId = PatientId;

            return _doctorService.GetPatientData(request);
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



            return _doctorService.GetPatientHistoryDateRange(request);

        }

        [HttpGet]
        [Route("doctorgetwarnings")]
        public DoctorGetWarningsResponse DoctorGetWarnings()
        {
            DoctorGetWarningsRequest request = new DoctorGetWarningsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _doctorService.DoctorGetWarnings(request);
        }

        [HttpGet]
        [Route("getwarnings")]
        public WarningListResponse GetWarningList()
        {
            WarningListRequest request = new WarningListRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _doctorService.GetWarningList(request);
        }

        [HttpGet]
        [Route("getconnections")]
        public DoctorGetRelationsResponse GetConnections()
        {
            var request = new DoctorGetRelationsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _doctorService.GetConnections(request);
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

            return _doctorService.ConnectionAcceptRefuse(request);
        }

        [HttpGet]
        [Route("deletepatient/{patientid}")]
        public PatientDeleteResponse DeletePatient(long patientid)
        {
            var request = new PatientDeleteRequest();
            request.PatientId = patientid;
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _doctorService.DeletePatient(request);

        }
    }
}