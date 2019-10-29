#     figure.asm     #
# This progam is designed to generate a set of coordinates that when plotted draw a stick figure
# author: Luke McDaniel

# Main
addi $sp, $0, 8192		# initialize stack pointer to top of memory
addi $s7, $0, 0		# initialize plot index to bottom of memory

addi $a0, $0, 30	# circle(30, 100, 20) head
addi $a1, $0, 100
addi $a2, $0, 20
jal circle

addi $a0, $0, 30	# line(30, 80, 30, 30) body
addi $a1, $0, 80
addi $a2, $0, 30
addi $a3, $0, 30
jal line

addi $a0, $0, 20	# line(20, 1, 30, 30) left leg
addi $a1, $0, 1
addi $a2, $0, 30
addi $a3, $0, 30
jal line

addi $a0, $0, 40	# line(40, 1, 30, 30) right leg
addi $a1, $0, 1
addi $a2, $0, 30
addi $a3, $0, 30
jal line

addi $a0, $0, 15	# line(15, 60, 30, 50) left arm
addi $a1, $0, 60
addi $a2, $0, 30
addi $a3, $0, 50
jal line

addi $a0, $0, 30	# line(30, 50, 45, 60) right arm
addi $a1, $0, 50
addi $a2, $0, 45
addi $a3, $0, 60
jal line

addi $a0, $0, 24	# circle(24, 105, 3) left eye
addi $a1, $0, 105
addi $a2, $0, 3
jal circle

addi $a0, $0, 36	# circle(36, 105, 3) right eye
addi $a1, $0, 105
addi $a2, $0, 3
jal circle

addi $a0, $0, 25	# line(25, 90, 35, 90) mouth center
addi $a1, $0, 90
addi $a2, $0, 35
addi $a3, $0, 90
jal line

addi $a0, $0, 25	# line(25, 90, 20, 95) mouth left
addi $a1, $0, 90
addi $a2, $0, 20
addi $a3, $0, 95
jal line

addi $a0, $0, 35	# line(35, 90, 40, 95) mouth right
addi $a1, $0, 90
addi $a2, $0, 40
addi $a3, $0, 95
jal line
j end

# absolute value function
# returns abs(num) in v0

abs: or $t8, $0, $a0
slt $t9, $t8, $0
beq $t9, $0, absIF	# break when a0 is positive
sub $v0, $0, $t8
j abs_END
absIF:
addi $v0, $t8, 0
abs_END: jr $ra

# line function
# uses $a0 - $a3, $s0 - $s3, $s7 is plot index
# x0 = $a0, y0 = $a1, x1 = $a2, y2 = $a3

line:
sub $t0, $a3, $a1	# y1 - y0 -> $t0
addi $sp, $sp, -2	# save a0 and ra before calling abs function
sw $a0, 0($sp)
sw $ra, 1($sp)
addi $a0, $t0, 0	# $t0 -> $a0 as abs arguement
jal abs
addi $s0, $v0, 0	# abs(y1 - y0) -> $s0
lw $ra, 1($sp)
lw $a0, 0($sp)
addi $sp, $sp, 2	# restore $a0 and $ra
sub $t0, $a2, $a0	# x1 - x0 -> $t0
addi $sp, $sp, -2	# save $a0 and $ra before calling abs
sw $a0, 0($sp)
sw $ra, 1($sp)
addi $a0, $t0, 0	# $t0 -> $a0 as abs arguement
jal abs
addi $s1, $v0, 0	# abs(x1 - x0) -> $s1
lw $ra, 1($sp)
lw $a0, 0($sp)
addi $sp, $sp, 2	# restore $a0 and $ra
slt $t0, $s1, $s0	# test abs(x1 -x0) < abs(y1 - y0)
beq $t0, $0, a_else	# if false goto else
addi $s2, $0, 1		# st = 1 -> $s2
j endif_a
a_else: addi $s2, $0, 0		# st = 0 -> $s2
endif_a:
beq $s2, $0, firstswap	# if st = 0 dont do first swap
addi $t2, $a0, 0	# put x0 in t2 temp	swap(x0, y0)
addi $a0, $a1, 0	# put y0 in x0
addi $a1, $t2, 0	# put x0 in y0
addi $t2, $a2, 0	# put x1 in t2 temp	swap(x1, y1)
addi $a2, $a3, 0	# put y1 in x1
addi $a3, $t2, 0	# put x1 in y1
firstswap: slt $t0, $a2, $a0	# test x1 < x0
beq $t0, $0, secondswap	# if x1 > x0 dont do first swap
addi $t2, $a0, 0	# put x0 in t2 temp	swap(x0, x1)
addi $a0, $a2, 0	# put x1 in x0
addi $a2, $t2, 0	# put x0 in x1
addi $t2, $a1, 0	# put y0 in t2 temp	swap(y0, y1)
addi $a1, $a3, 0	# put y1 in y0
addi $a3, $t2, 0	# put y0 in y1
secondswap:
sub $s0, $a2, $a0	# deltax = x1 - x0 -> $s0
addi $sp, $sp, -2	# save a0 and ra before calling abs(y1 - y0)
sw $a0, 0($sp)
sw $ra, 1($sp)
sub $a0, $a3, $a1	# y1 - y0 -> $a0 as abs argument
jal abs
addi $s1, $v0, 0	# deltay = abs(y1 - y0) -> $s1
lw $ra, 1($sp)
lw $a0, 0($sp)
addi $sp, $sp, 2	# restore $a0 and $ra
addi $s3, $0, 0		# error = 0 -> $s3
addi $s4, $a1, 0	# y = y0 -> $s4
slt $t2, $a1, $a3	# test y0 < y1
beq $t2, $0, b_else	# if false goto else
addi $s5, $0, 1		# ystep = 1 -> $s5
j endif_b
b_else:
addi $s5, $0, -1	# ystep = -1 -> $s5
endif_b:
addi $s6, $a0, 0	# x = x0 -> $s6
loopone:
addi $t4, $a2, 1	# x1 + 1 (end of loop) -> $t4
beq $s6, $t4, endloopone # break loop after x1 is processed
beq $s2, $0, c_else	# if (st == 1) is false goto else
sw $s4, 0($s7)		# plot(y, x)
addi $s7, $s7, 1
sw $s6, 0($s7)
addi $s7, $s7, 1
j endif_c
c_else:
sw $s6, 0($s7)		# plot(x, y)
addi $s7, $s7, 1
sw $s4, 0($s7)
addi $s7, $s7, 1
endif_c: 
add $s3, $s3, $s1	# error = error + deltay
sll $t5, $s3, 1		# 2 * error -> $t5
addi $t6, $s0, 1	# deltax + 1 -> $t6
slt $t7, $t6, $t5	# test deltax =< 2*error
beq $t7, $0, varedits	# break when false
add $s4, $s4, $s5	# y = y + ystep
sub $s3, $s3, $s0	# error = error - deltax
varedits: 
addi $s6, $s6, 1
j loopone
endloopone:
jr $ra


# circle function
# uses $a0 - $a2, $s0 - $s2, uses $s7 as plot index
# xc = $a0, yc = $a1, r = $a2
circle:
addi $t0, $0, 0		# x = 0 -> $t0
addi $t1, $a2, 0	# y = r -> $t1
sll $t4, $a2, 1		# 2 * r -> $t4
addi $t5, $0, 3
sub $t2, $t5, $t4	# g = 3 - 2*r -> $t2
sll $t4, $a2, 2 	# 4 * r -> $t4
addi $t5, $0, 10
sub $t3, $t5, $t4	# diagonalInc = 10 - 4*r -> $t3
addi $t4, $0, 6		# rightInc = 6 -> $t4
looptwo:
addi $t5, $t1, 1	# y + 1 -> $t5
slt $t6, $t0, $t5	# test x <= y
beq $t6, $0, endlooptwo

add $t7, $a0, $t0	# plot(xc + x, yc + y)
sw $t7, 0($s7)
addi $s7, $s7, 1
add $t8, $a1, $t1
sw $t8, 0($s7)
addi $s7, $s7, 1

add $t7, $a0, $t0	# plot(xc + x, yc - y)
sw $t7, 0($s7)
addi $s7, $s7, 1
sub $t8, $a1, $t1
sw $t8, 0($s7)
addi $s7, $s7, 1

sub $t7, $a0, $t0	# plot(xc - x, yc + y)
sw $t7, 0($s7)
addi $s7, $s7, 1
add $t8, $a1, $t1
sw $t8, 0($s7)
addi $s7, $s7, 1

sub $t7, $a0, $t0	# plot(xc - x, yc - y)
sw $t7, 0($s7)
addi $s7, $s7, 1
sub $t8, $a1, $t1
sw $t8, 0($s7)
addi $s7, $s7, 1

add $t7, $a0, $t1	# plot(xc + y, yc + x)
sw $t7, 0($s7)
addi $s7, $s7, 1
add $t8, $a1, $t0
sw $t8, 0($s7)
addi $s7, $s7, 1

add $t7, $a0, $t1	# plot(xc + y, yc - x)
sw $t7, 0($s7)
addi $s7, $s7, 1
sub $t8, $a1, $t0
sw $t8, 0($s7)
addi $s7, $s7, 1

sub $t7, $a0, $t1	# plot(xc - y, yc + x)
sw $t7, 0($s7)
addi $s7, $s7, 1
add $t8, $a1, $t0
sw $t8, 0($s7)
addi $s7, $s7, 1

sub $t7, $a0, $t1	# plot(xc - y, yc - x)
sw $t7, 0($s7)
addi $s7, $s7, 1
sub $t8, $a1, $t0
sw $t8, 0($s7)
addi $s7, $s7, 1

addi $t4, $0, -1
slt $t5, $t4, $t2	# test 0 <= g
beq $t5, $0, else_d	# if false goto else
add $t2, $t2, $t3	# g += diagonalInc
addi $t3, $t3, 8	# diagonalInc += 8
addi $t1, $t1, -1	# y-= 1
j endif_d
else_d:
add $t2, $t2, $t4	# g += rightInc
addi $t3, $t3, 4	# diagonalInc += 4
endif_d:
addi $t4, $t4, 4	# rightInc += 4
addi $t0, $t0, 1	# x += 1
j looptwo
endlooptwo: 
jr $ra

end:
