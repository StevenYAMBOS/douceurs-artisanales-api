import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { AUTH } from 'src/config/constants';

@Injectable()
export class AuthGuard extends PassportStrategy(Strategy) {
  constructor() {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      secretOrKey: AUTH.JWT_SECRET_KEY,
    });
  }

  async validate(payload: any) {
    return { userId: payload.id };
  }
}
