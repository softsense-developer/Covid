using System.Collections.Generic;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;

namespace Smartsense.Business.Abstract
{
    public interface ISupervisorService
    {
     GetDoctorsResponse GetDoctorsInfo(SupervisorGetDoctorsRequest request);

     SupervisorPromotionResponse DoctorPromotion(SupervisorPromotionRequest request);

     SupervisorDemotionResponse DoctorDemotion(SupervisorDemotionRequest request);

     SupervisorGetLocationsResponse GetSupervisorLocations(SupervisorGetLocationsRequest request);

     PutHospitalResponse PutHospital(PutHospitalRequest request);

     GetSupervisorHospitalResponse GetHospital(GetSupervisorHospitalRequest request);

     GetHospitalDoctorsResponse GetHospitalDoctors(GetHospitalDoctorsRequest request);

     GetPatientDataResponse GetPatientData(GetPatientDataRequest request);

     GetPatientDateRangeResponse GetPatientHistoryDateRange(GetPatientDateRangeRequest request);

    }
}