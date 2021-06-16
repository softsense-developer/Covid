using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.DTO;

namespace Smartsense.Business.Abstract
{
    public interface IValueService
    {
        ValueResponse AddValue(ValueRequest request);

        ValueHistoryResponse GetHistory(ValueHistoryRequest request);

        GetDateRangeResponse GetHistoryDateRange(GetDateRangeRequest request);

    }
}
