using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("hospitals")]
    public class Hospital:BaseEntity
    {
        [Column("hospital_name")]
        public string HospitalName { get; set; }

        [Column("hospital_address")]
        public string HospitalAddress { get; set; }

        [Column("hospital_capacity")]
        public long HospitalCapacity { get; set; }

    }
}
