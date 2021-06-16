using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class CompanionWarningsResponse:BaseApiResponse
    {
        public List<WarningInfoModel> Warnings { get; set; }

        public CompanionWarningsResponse()
        {
            this.Warnings = new List<WarningInfoModel>();
        }

    }
}
