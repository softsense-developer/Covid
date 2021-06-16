using System.Collections.Generic;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;

namespace Smartsense.Business.Abstract
{
    public interface IDoctorService
    {
        DoctorGetPatientsResponse GetInfo(DoctorGetPatientsRequest request);

        PatientValueResponse GetPatientData(PatientValueRequest request);

        DoctorGetRelationsResponse GetConnections(DoctorGetRelationsRequest request);

        ConnectionAcceptRefuseResponse ConnectionAcceptRefuse(ConnectionAcceptRefuseRequest request);

        DoctorGetLocationsResponse GetDoctorLocations(DoctorGetLocationsRequest request);

        DoctorGetWarningsResponse DoctorGetWarnings(DoctorGetWarningsRequest request);

        PatientDeleteResponse DeletePatient(PatientDeleteRequest request);

        WarningListResponse GetWarningList(WarningListRequest request);
        GetPatientDateRangeResponse GetPatientHistoryDateRange(GetPatientDateRangeRequest request);

    }
}