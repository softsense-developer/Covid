using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class DoktorlarInfoModel
    {
        public string DoktorAdi { get; set; }
        public long DoktorID { get; set; }
        public List<HastalarInfoModel> HastalarInfoModels { get; set; }

        public DoktorlarInfoModel()
        {
           this.HastalarInfoModels = new List<HastalarInfoModel>();
        }
    }
}
