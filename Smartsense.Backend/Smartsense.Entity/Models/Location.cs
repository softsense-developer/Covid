using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("locations")]
    public class Location:BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("latitude_quarantine")]
        public double LatitudeQuarantine { get; set; }

        [Column("longitude_quarantine")]
        public double LongitudeQuarantine { get; set; }

        [Column("latitude_now")]
        public double LatitudeNow { get; set; }

        [Column("longitude_now")]
        public double LongitudeNow { get; set; }

        [Column("quarantine_start")]
        public DateTime QuarantineStart { get; set; }

        [Column("quarantine_end")]
        public DateTime QuarantineEnd { get; set; }
    }
}
