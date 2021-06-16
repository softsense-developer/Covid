using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("companions")]
    public class Companion:BaseEntity
    {
        [Column("companion_user_id")]
        public long CompanionUserId { get; set; }

        [Column("patient_user_id")]
        public long PatientUserId { get; set; }
    }
}
