import { Injectable, BadRequestException } from '@nestjs/common';
import { UserService } from '../user/user.service';
import * as bcrypt from 'bcrypt';
import { UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(
    private readonly usersService: UserService,
    private readonly jwtService: JwtService,
  ) {}

  async register(
    first_name: string,
    last_name: string,
    email: string,
    phone: string,
    password: string,
  ) {
    const existingUser = await this.usersService.findByEmail(email);
    if (existingUser) {
      throw new BadRequestException(`L'adresse email est déjà utilisée.`);
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    return this.usersService.register({
      first_name,
      last_name,
      email,
      phone,
      password: hashedPassword,
      role: 'user',
      created_at: new Date(),
      updated_at: new Date(),
    });
  }

  async login(email: string, password: string) {
    const user = await this.usersService.findByEmail(email);
    if (!user) {
      throw new UnauthorizedException(
        `Informations d'identification incorrectes`,
      );
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      throw new UnauthorizedException(
        `Informations d'identification incorrectes`,
      );
    }

    const token = this.jwtService.sign({ id: user.id });
    return { token };
  }
}
