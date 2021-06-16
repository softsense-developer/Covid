using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Models;

namespace Smartsense.Entity.DTO
{
    public class GetHospitalResponse:BaseApiResponse
    {
        public List<Hospital> Hospitals { get; set; }

        public GetHospitalResponse()
        {
            this.Hospitals = new List<Hospital>();
        }
    }
}
