using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetCompanionResponse :BaseApiResponse
    {
        public class CompanionModel
        {
            public long Userid { get; set; }

            public string Name { get; set; }

            public string Surname { get; set; }

            public string Email { get; set; }
        }

        public List<CompanionModel> CompanionModels { get; set; }

        public  GetCompanionResponse()
        {
            this.CompanionModels = new List<CompanionModel>();
        }

    }
}
