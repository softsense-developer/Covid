using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class CompanionGetWarningsResponse:BaseApiResponse
    {
        public List<PatientInfoModel> Patients { get; set; }

        public CompanionGetWarningsResponse()
        {
            this.Patients = new List<PatientInfoModel>();
        }
    }
}
