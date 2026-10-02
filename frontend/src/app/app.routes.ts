import { Routes } from '@angular/router';
import { Home } from './home/home';
import { LoginUser } from './login-user/login-user';
import { LoginAdmin } from './login-admin/login-admin';
import { Register } from './register/register';
import { ForgottenPassword } from './forgotten-password/forgotten-password';
import { Details } from './details/details';
import { AthleteProfile } from './athletePage/athlete-profile/athlete-profile';
import { AthleteBooking } from './athletePage/athlete-booking/athlete-booking';
import { AthleteShop } from './athletePage/athlete-shop/athlete-shop';
import { AthleteReviews } from './athletePage/athlete-reviews/athlete-reviews';
import { AthleteSparing } from './athletePage/athlete-sparing/athlete-sparing';
import { AthleteStatistics } from './athletePage/athlete-statistics/athlete-statistics';
import { AthleteDetails } from './athletePage/athlete-details/athlete-details';
import { WorkerProfile } from './workerPage/worker-profile/worker-profile';
import { WorkerReservationTraining } from './workerPage/worker-reservation-training/worker-reservation-training';
import { WorkerPromotionsEquipment } from './workerPage/worker-promotions-equipment/worker-promotions-equipment';
import { WorkerCalendar } from './workerPage/worker-calendar/worker-calendar';
import { WorkerReport } from './workerPage/worker-report/worker-report';
import { AthleteTraining } from './athletePage/athlete-training/athlete-training';
import { WorkerFacilities } from './workerPage/worker-facilities/worker-facilities';
import { AdminAccounts } from './adminPage/admin-accounts/admin-accounts';
import { AdminRequests } from './adminPage/admin-requests/admin-requests';
import { AdminSports } from './adminPage/admin-sports/admin-sports';
import { AdminFacilityRequest } from './adminPage/admin-facility-request/admin-facility-request';
import { AdminTrainers } from './adminPage/admin-trainers/admin-trainers';

export const routes: Routes = [
    {path: "", component: Home},
    {path: "loginUser", component: LoginUser},
    {path: "system-access", component: LoginAdmin},
    {path: "register", component: Register},
    {path: "forgottenPassword", component: ForgottenPassword},
    {path: "athleteProfile", component: AthleteProfile},
    {path: "athleteBooking", component: AthleteBooking},
    {path: "athleteShop", component: AthleteShop},
    {path: "athleteReviews", component: AthleteReviews},
    {path: "athleteSparing", component: AthleteSparing},
    {path: "athleteStatistics", component: AthleteStatistics},
    {path: "athleteTraining", component: AthleteTraining},
    {path: "workerProfile", component: WorkerProfile},
    {path: "workerFacilities", component: WorkerFacilities},
    {path: "workerReservationTraining", component: WorkerReservationTraining},
    {path: "workerPromotionEquipment", component: WorkerPromotionsEquipment},
    {path: "workerReport", component: WorkerReport},
    {path: "workerCalendar", component: WorkerCalendar},
    {path: "details", component: Details},
    {path: 'athleteDetails', component: AthleteDetails},
    {path: 'adminAccounts', component: AdminAccounts},
    {path: 'adminRequests', component: AdminRequests},
    {path: 'adminSports', component: AdminSports},
    {path: 'adminFacility', component: AdminFacilityRequest},
    {path: 'adminTrainers', component: AdminTrainers}
];
