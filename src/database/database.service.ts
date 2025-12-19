// database.service.ts
import { Injectable, Inject } from '@nestjs/common';

@Injectable()
export class DatabaseService {
  constructor(@Inject('POSTGRES_POOL') private readonly sql: any) {}
}
