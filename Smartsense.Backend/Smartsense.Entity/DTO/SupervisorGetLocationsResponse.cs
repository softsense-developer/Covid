using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class SupervisorGetLocationsResponse:BaseApiResponse
    {
        public List<PatientLocationModel> SupervisorPatients { get; set; }


        public SupervisorGetLocationsResponse()
        {
            this.SupervisorPatients = new List<PatientLocationModel>();
        }
    }
}
