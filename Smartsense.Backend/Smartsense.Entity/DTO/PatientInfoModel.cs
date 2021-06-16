using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PatientInfoModel
    {
        public string Name { get; set; }
        public string Surname { get; set; }
        public long UserId { get; set; }
        public List<ValueTypeListModel> ValueTypeListModels { get; set; }

        public PatientInfoModel()
        {
            this.ValueTypeListModels = new List<ValueTypeListModel>();
        }
    }
}
