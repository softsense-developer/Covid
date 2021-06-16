using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.DTO;

namespace Smartsense.Business.Abstract
{
    public interface ICompanionService
    {
        CompanionGetPatientsResponse GetInfo(CompanionGetPatientsRequest request);

        CompanionGetLocationsResponse GetCompanionLocations(CompanionGetLocationsRequest request);

        PatientValueResponse GetPatientData(PatientValueRequest request);

        CompanionGetWarningsResponse CompanionGetWarnings(CompanionGetWarningsRequest request);

        CompanionWarningsResponse WarningList(CompanionWarningsRequest request);
    }
}
