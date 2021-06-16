using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class DoctorGetRelationsResponse:BaseApiResponse
    {
        public List<ConnectionRequestModel> ConnectionRequests { get; set; }
    

        public DoctorGetRelationsResponse()
        {
            this.ConnectionRequests = new List<ConnectionRequestModel>();
        }

    }
}
