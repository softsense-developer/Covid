using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.Models
{
    [Table("warnings")]
    public class Warning:BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("warning_data")]
        public WarningType WarningData { get; set; }

        [Column("warning_value")]
        public decimal WarningValue { get; set; }

        [Column("save_date")]
        public DateTime SaveDate { get; set; }
    }
}
