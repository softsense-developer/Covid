using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetHospitalDoctorsResponse:BaseApiResponse
    {
        public List<DoctorModel> Doctors { get; set; }

        public GetHospitalDoctorsResponse()
        {
            this.Doctors = new List<DoctorModel>();
        }
    }
}
