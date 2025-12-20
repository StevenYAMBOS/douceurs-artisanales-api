import { Injectable, BadRequestException } from '@nestjs/common';
import { UserService } from '../user/user.service';
import * as bcrypt from 'bcrypt';
import { UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import {
  APP_CONFIG,
  ERROR_MESSAGES,
  USER_MESSAGES,
} from 'src/config/constants';

@Injectable()
export class AuthService {
  constructor(
    private readonly usersService: UserService,
    private readonly jwtService: JwtService,
  ) {}

  async register(username: string, email: string, password: string) {
    const existingUser = await this.usersService.findByEmail(email);
    if (existingUser) {
      throw new BadRequestException(USER_MESSAGES.EMAIL_ALREADY_EXISTS);
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const userRole: string = 'user';
    return this.usersService.register({
      username,
      email,
      password: hashedPassword,
      role: userRole,
      last_login: APP_CONFIG.DATE_NOW,
      created_at: APP_CONFIG.DATE_NOW,
      updated_at: APP_CONFIG.DATE_NOW,
    });
  }

  async login(email: string, password: string) {
    const user = await this.usersService.findByEmail(email);
    if (!user) {
      throw new UnauthorizedException(ERROR_MESSAGES.INVALID_EMAIL);
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      throw new UnauthorizedException(ERROR_MESSAGES.INVALID_PASSWORD);
    }

    const token = this.jwtService.sign({ id: user.id });
    return { token };
  }
}
