package com.shutterbug.spacearcade.emulator;
import java.io.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.shutterbug.spacearcade.*;

public class Intel8080cpu
{
	private char regs[];
	private char[] mem;
	private char[] flags;
	private int sp;
	private int pc;
	private int i;
	private int cycles;
	public void reset() throws IOException{
		regs = new char[5];
		flags = new char[7];
		mem = new char[0x4000];
		pc = 0;
		sp = 0;
		DataInputStream input = null;
		try {
//			input = new DataInputStream(java.lang.Thread.currentThread().getContextClassLoader().getResourceAsStream("sdcard/inv.h"));
			FileHandle file = Gdx.files.internal("sdcard/inv.h");
			
			int offset = 0;
			byte[] filearray = file.readBytes();
			for(i = 0; i < 0x7ff; i++){
				mem[i] = (char) (filearray[offset] & 0xFF);
				offset++;
			}

		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		
		try {
//			input = new DataInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("sdcard/inv.g"));
			FileHandle file = Gdx.files.internal("sdcard/inv.g");
			
			int offset = 0;
			byte[] filearray = file.readBytes();
			for(i = 0x7FF; i < (0x7ff * 2); i++){
				mem[i] = (char) (filearray[offset] & 0xFF);
				offset++;
			}

		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		
		try {
//			input = new DataInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("sdcard/inv.f"));
			FileHandle file = Gdx.files.internal("sdcard/inv.f");

			int offset = 0;
			byte[] filearray = file.readBytes();
			for(i = (0x7ff * 2); i < (0x7ff * 3); i++){
				mem[i] = (char) (filearray[offset] & 0xFF);
				offset++;
			}

		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		
		try {
//			input = new DataInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("sdcard/inv.e"));
			FileHandle file = Gdx.files.internal("sdcard/inv.e");

			int offset = 0;
			byte[] filearray = file.readBytes();
			for(i = (0x7ff * 3); i < (0x7ff * 4); i++){
				mem[i] = (char) (filearray[offset] & 0xFF);
				offset++;
			}

		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		}
		
		public void run(){
			switch(mem[pc]){
				case 0x00:{
					//do nothing at all
					pc++;
					cycles = 4;
					while(cycles > 0){
						cycles--;
					}
					break;
				}
				
				case 0x06:{
					regs[Register.B.index] = (mem[pc + 1]);
						cycles = 7;
						while(cycles > 0){
							cycles--;
						}
						pc++;
					break;
				}
				
				case 0x0D:{
					regs[Register.C.index]--;
					pc++;
					break;
				}
				
				case 0x32:{
						int word = (mem[pc + 2] << 8) | (mem[pc + 1]);
						mem[word] = regs[Register.B.index];
						//pc++;
						//start debug for pc check
						//MyGdxGame.halt = true;
						//MyGdxGame.debug = Integer.toHexString(pc);
						//MyGdxGame.debug2 = Integer.toHexString(word);
						//end
						
						//Increment over ALL of opcode
						pc += 3;
						break;
					}
				
				case 0x3C:{
					regs[Register.A.index]++;
					pc++;
					break;
				}
				
				case 0x3E:{
						regs[Register.A.index] = (mem[pc + 1]);
						cycles = 7;
						while(cycles > 0){
							cycles--;
						}
						pc++;
						break;
					}
				
				case 0xc2:{
					if(flags[Flag.Zero.index] == 1){
						Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
						pc = ((mem[pc + 2] << 8) | (mem[pc +1]));
						cycles = 10;
						while(cycles > 0){
							cycles--;
						}
					}
					pc++;
						break;
					}
					
					
				case 0xc3:{
						Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
					pc = ((mem[pc + 2] << 8) | (mem[pc +1]));
						cycles = 10;
						while(cycles > 0){
							cycles--;
						}
						break;
						}
				
				case 0xc9:{
					//Possible erronous emulation.
					pc = ((sp << 8) | (sp + 1));
					sp += 2;
					break;
					}
						
				case 0xcd:{
							sp = (mem[pc + 2]);
							pc = mem[pc];
				break;
				}
				
				case 0xd3:{
					mem[pc + 1] = regs[Register.A.index];
					pc++;
					break;
				}
					
				default:
				{
				//	Gdx.app.log("Unknown opcode:", Integer.toHexString(mem[pc]));
				MyGdxGame.str = Integer.toHexString(mem[pc]);
				MyGdxGame.pc = Integer.toHexString(pc);
				MyGdxGame.halt = true;
				MyGdxGame.debug = "Halted.";
				//System.exit(0);
					}
			}
			}
			
}
