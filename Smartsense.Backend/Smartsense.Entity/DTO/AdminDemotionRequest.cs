using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class AdminDemotionRequest :BaseApiRequest
    {
        public long DemotionId { get; set; }
        public long HospitalId { get; set; }
        public long PromotionId { get; set; }
        
    }
}
