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
    [Route("api/companion")]
    [ApiController]
    public class CompanionController : ControllerBase
    {
        private readonly ICompanionService _companionService;

        public CompanionController(ICompanionService companionService)
        {
            _companionService = companionService;
        }

        [HttpGet]
        [Route("getCompanionpatients")]
        public CompanionGetPatientsResponse GetPatients()
        {
            var request = new CompanionGetPatientsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _companionService.GetInfo(request);

        }

        [HttpGet]
        [Route("getcompanionlocations")]
        public CompanionGetLocationsResponse GetCompanionLocations()
        {
            CompanionGetLocationsRequest request = new CompanionGetLocationsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _companionService.GetCompanionLocations(request);
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

            return _companionService.GetPatientData(request);
        }

        [HttpGet]
        [Route("companiongetwarnings")]
        public CompanionGetWarningsResponse CompanionGetWarnings()
        {
            var request = new CompanionGetWarningsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _companionService.CompanionGetWarnings(request);

        }

        [HttpGet]
        [Route("warninglist")]
        public CompanionWarningsResponse WarningList()
        {
            var request = new CompanionWarningsRequest();
            var identity = User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var id = Convert.ToInt64(identity.Claims.ElementAt(0).Value);
                request.UserId = id;
            }

            return _companionService.WarningList(request);
        }
    }
}