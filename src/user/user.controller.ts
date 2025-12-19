import { Controller, UseGuards, Get, Request } from '@nestjs/common';

import { AuthGuard } from '../auth/auth.guard';

@Controller('user')
@UseGuards(AuthGuard)
export class ProfileController {
  @Get()
  getProfile(@Request() req) {
    return req.user;
  }
}
