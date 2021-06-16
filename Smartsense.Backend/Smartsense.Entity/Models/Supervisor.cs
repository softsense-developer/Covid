using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("supervisors")]
    public class Supervisor :BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("hospital_id")]
        public long HospitalId { get; set; }

    }
}
