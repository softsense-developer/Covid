using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class DoctorGetWarningsResponse:BaseApiResponse
    {
        public List<PatientInfoModel> Patients { get; set; }

        public DoctorGetWarningsResponse()
        {
            this.Patients = new List<PatientInfoModel>();
        }
    }
}
