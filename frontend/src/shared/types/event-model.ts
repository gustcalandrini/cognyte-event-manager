import { EventStatus } from './enumerations/event-enum';
import dayjs from 'dayjs';

export interface IEvent {
  id?: number;
  title: string;
  startDate: dayjs.Dayjs;
  endDate: dayjs.Dayjs;
  price: string; // Represented as string to match BigDecimal safely
  status: EventStatus;
}
