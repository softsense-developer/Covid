using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PatientRelationDoctorRequest : BaseApiRequest
    {
        public long DoctorId { get; set; }
    }
}
