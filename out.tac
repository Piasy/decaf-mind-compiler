VTABLE(_Main) {
    <empty>
    Main
    _Main.test;
}

VTABLE(_AAA) {
    _Main
    AAA
    _AAA.test;
}

FUNCTION(_Main_New) {
memo ''
_Main_New:
    _T2 = 12
    parm _T2
    _T3 =  call _Alloc
    _T4 = 0
    *(_T3 + 4) = _T4
    *(_T3 + 8) = _T4
    _T5 = VTBL <_Main>
    *(_T3 + 0) = _T5
    return _T3
}

FUNCTION(_AAA_New) {
memo ''
_AAA_New:
    _T6 = 12
    parm _T6
    _T7 =  call _Alloc
    _T8 = 0
    *(_T7 + 4) = _T8
    *(_T7 + 8) = _T8
    _T9 = VTBL <_AAA>
    *(_T7 + 0) = _T9
    return _T7
}

FUNCTION(main) {
memo ''
main:
    _T11 =  call _Main_New
    _T10 = _T11
    parm _T10
    _T12 = *(_T10 + 0)
    _T13 = *(_T12 + 8)
    _T14 =  call _T13
    parm _T14
    call _PrintInt
    _T15 = "\n"
    parm _T15
    call _PrintString
}

FUNCTION(_Main.test) {
memo '_T0:4'
_Main.test:
    _T17 = 100
    _T16 = _T17
    parm _T16
    call _PrintInt
    _T18 = "\n"
    parm _T18
    call _PrintString
    _T19 = *(_T0 + 4)
    _T20 = 10
    _T21 = 0
    _T22 = (_T20 < _T21)
    if (_T22 == 0) branch _L13
    _T23 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T23
    call _PrintString
    call _Halt
_L13:
    _T24 = 4
    _T25 = (_T24 * _T20)
    _T26 = (_T24 + _T25)
    parm _T26
    _T27 =  call _Alloc
    *(_T27 + 0) = _T20
    _T28 = 0
    _T27 = (_T27 + _T26)
_L14:
    _T26 = (_T26 - _T24)
    if (_T26 == 0) branch _L15
    _T27 = (_T27 - _T24)
    *(_T27 + 0) = _T28
    branch _L14
_L15:
    *(_T0 + 4) = _T27
    _T30 = 0
    _T29 = _T30
_L16:
    _T31 = 10
    _T32 = (_T29 < _T31)
    if (_T32 == 0) branch _L17
    _T33 = "before assign: this.a[i] = "
    parm _T33
    call _PrintString
    _T34 = *(_T0 + 4)
    _T35 = *(_T34 - 4)
    _T36 = (_T29 < _T35)
    if (_T36 == 0) branch _L18
    _T37 = 0
    _T38 = (_T29 < _T37)
    if (_T38 == 0) branch _L19
_L18:
    _T39 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T39
    call _PrintString
    call _Halt
_L19:
    _T40 = 4
    _T41 = (_T29 * _T40)
    _T42 = (_T34 + _T41)
    _T43 = *(_T42 + 0)
    parm _T43
    call _PrintInt
    _T44 = *(_T0 + 4)
    _T45 = *(_T44 - 4)
    _T46 = (_T29 < _T45)
    if (_T46 == 0) branch _L20
    _T47 = 0
    _T48 = (_T29 < _T47)
    if (_T48 == 0) branch _L21
_L20:
    _T49 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T49
    call _PrintString
    call _Halt
_L21:
    _T50 = 4
    _T51 = (_T29 * _T50)
    _T52 = (_T44 + _T51)
    _T53 = *(_T52 + 0)
    _T54 = 4
    _T55 = (_T29 * _T54)
    _T56 = (_T44 + _T55)
    *(_T56 + 0) = _T29
    _T57 = ", after assign: this.a[i] = "
    parm _T57
    call _PrintString
    _T58 = *(_T0 + 4)
    _T59 = *(_T58 - 4)
    _T60 = (_T29 < _T59)
    if (_T60 == 0) branch _L22
    _T61 = 0
    _T62 = (_T29 < _T61)
    if (_T62 == 0) branch _L23
_L22:
    _T63 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T63
    call _PrintString
    call _Halt
_L23:
    _T64 = 4
    _T65 = (_T29 * _T64)
    _T66 = (_T58 + _T65)
    _T67 = *(_T66 + 0)
    parm _T67
    call _PrintInt
    _T68 = "\n"
    parm _T68
    call _PrintString
    _T69 = 1
    _T70 = (_T29 + _T69)
    _T29 = _T70
    branch _L16
_L17:
    _T71 = 0
    _T29 = _T71
_L24:
    _T72 = 10
    _T73 = (_T29 < _T72)
    if (_T73 == 0) branch _L25
    _T74 = "confirm: this.a[i] = "
    parm _T74
    call _PrintString
    _T75 = *(_T0 + 4)
    _T76 = *(_T75 - 4)
    _T77 = (_T29 < _T76)
    if (_T77 == 0) branch _L26
    _T78 = 0
    _T79 = (_T29 < _T78)
    if (_T79 == 0) branch _L27
_L26:
    _T80 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T80
    call _PrintString
    call _Halt
_L27:
    _T81 = 4
    _T82 = (_T29 * _T81)
    _T83 = (_T75 + _T82)
    _T84 = *(_T83 + 0)
    parm _T84
    call _PrintInt
    _T85 = "\n"
    parm _T85
    call _PrintString
    _T86 = 1
    _T87 = (_T29 + _T86)
    _T29 = _T87
    branch _L24
_L25:
    return _T16
}

FUNCTION(_AAA.test) {
memo '_T1:4'
_AAA.test:
    _T89 = 100
    _T88 = _T89
    return _T88
}

