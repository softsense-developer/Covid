using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.Models
{
    [Table("values")]
    public class Value:BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("data_type")]
        public DataValueType DataType { get; set; }

        [Column("data_value")]
        public double DataValue { get; set; }

        [Column("save_date")]
        public DateTime SaveDate { get; set; }

        
    }
}
