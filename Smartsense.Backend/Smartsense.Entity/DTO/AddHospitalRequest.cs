using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class AddHospitalRequest:BaseApiRequest
    {
        public string HospitalName { get; set; }

        public string Address { get; set; }

        public long HospitalCapacity { get; set; }
    }
}
