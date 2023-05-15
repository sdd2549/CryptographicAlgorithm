Hex = "0123456789ABCDEF"
MasterKey = [4, 4, 5, 5, 4, 14, 4, 5]
S_Box = [14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7]
P_Box = [1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16]

def main():
    s = input("input : 0x")
    input_ = [Hex.index(s[i]) for i in range(4)]
    res = SPN_Cipher(input_)
    print("result : 0x", end="")
    for i in range(4):
        print(Hex[res[i]], end="")
    print()

def SPN_Cipher(input_):
    plain_text = input_
    for i in range(3):
        plain_text = Xor_withMK(plain_text, i)
        plain_text = S_box(plain_text)
        plain_text = P_box(plain_text)
    plain_text = Xor_withMK(plain_text, 3)
    plain_text = S_box(plain_text)
    plain_text = Xor_withMK(plain_text, 4)
    return plain_text

def Xor_withMK(in_, round_):
    mk = MasterKey[round_:round_+4]
    print(f"Round{round_ + 1} key : 0x{''.join([Hex[i] for i in mk])}")
    input_hex = InttoHex(in_)
    mk_hex = InttoHex(mk)
    for i in range(16):
        input_hex[i] = input_hex[i] ^ mk_hex[i]
    res = HextoInt(input_hex)
    print(f"result of Xor with MK : 0x{''.join([Hex[i] for i in res])}")
    return res

def S_box(plain):
    plain = [S_Box[plain[i]] for i in range(4)]
    print(f"S_box : 0x{''.join([Hex[i] for i in plain])}")
    return plain

def P_box(plain):
    hex_ = InttoHex(plain)
    tmp = [hex_[P_Box[i]-1] for i in range(16)]
    res = HextoInt(tmp)
    print(f"P_box : 0x{''.join([Hex[i] for i in res])}")
    return res

def HextoInt(hex_):
    res = [0]*4
    for i in range(4):
        for j in range(4):
            res[i] *= 2
            res[i] += hex_[4*i+j]
    return res

def InttoHex(in_):
    hex_ = [0]*16
    for i in range(4):
        for j in range(4):
            hex_[4*i+j] = in_[i] // (2**(3-j)) % 2
    return hex_

if __name__ == '__main__':
    main()