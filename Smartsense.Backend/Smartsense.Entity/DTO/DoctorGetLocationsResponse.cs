using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class DoctorGetLocationsResponse :BaseApiResponse
    {
        
        public List<PatientLocationModel> Patients { get; set;}

        public DoctorGetLocationsResponse()
        {
            this.Patients = new List<PatientLocationModel>();
        }

    }
}
