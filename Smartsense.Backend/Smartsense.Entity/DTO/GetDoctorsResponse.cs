using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetDoctorsResponse:BaseApiResponse
    {
        public List<DoktorlarInfoModel> DoktorInfoModels { get; set; }

        public GetDoctorsResponse()
        {
            this.DoktorInfoModels = new List<DoktorlarInfoModel>();
        }
    }
}
