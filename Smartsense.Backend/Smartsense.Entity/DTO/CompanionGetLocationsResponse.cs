using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class CompanionGetLocationsResponse:BaseApiResponse
    {
        public List<PatientLocationModel> Patients { get; set; }

        public CompanionGetLocationsResponse()
        {
            this.Patients = new List<PatientLocationModel>();
        }

    }
}
