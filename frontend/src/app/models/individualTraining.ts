export interface IndividualTraining {
  id: number;
  athleteId: number;
  trainerId: number;
  facilityId: number;
  sportId: number;
  scheduledAt: string;
  status: string;
  trainingDate: string;
  timeFrom: string;
  timeTo: string;
  trainerName: string;
  facilityName: string;
  sportName: string;
}