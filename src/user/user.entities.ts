import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: number;

  @Column({ unique: true, length: 100 })
  email: string;

  @Column({ unique: false, length: 50 })
  first_name: string;

  @Column({ unique: false, length: 50 })
  last_name: string;

  @Column({ length: 255 })
  password_hash: string;

  @Column({ length: 15 })
  phone: string;

  @Column({ length: 20 })
  role: string;

  @Column()
  is_verified: boolean;

  @Column()
  last_login: Date;

  @Column()
  is_active: boolean;

  @Column()
  created_at: Date;

  @Column()
  updated_at: Date;
}
