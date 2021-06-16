using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Models;

namespace Smartsense.Entity.DTO
{
    public class GetAllHospitalResponse : BaseApiResponse
    {
        public List<Hospital> Hospitals { get; set; }

        public GetAllHospitalResponse()
        {
            this.Hospitals = new List<Hospital>();
        }
    }
}
