import { Controller, Post, Body, HttpCode, HttpStatus } from '@nestjs/common';
import { AuthService } from './auth.service';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('register')
  async register(
    @Body('first_name') first_name: string,
    @Body('last_name') last_name: string,
    @Body('email') email: string,
    @Body('password') password: string,
  ) {
    return this.authService.register(first_name, last_name, email, password);
  }

  @HttpCode(HttpStatus.OK)
  @Post('login')
  async login(
    @Body('email') email: string,
    @Body('password') password: string,
  ) {
    return this.authService.login(email, password);
  }
}
