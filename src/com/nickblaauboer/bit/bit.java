package com.nickblaauboer.bit;

public class bit implements IBit {

    private int value;

    public bit(int value) {
        this.value = value;
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    @Override
    public void toggle() {
        switch (this.value) {
            case 0:
                this.value = 1;
                break;
            case 1:
                this.value = 0;
                break;
        }
    }

    @Override
    public void set() {
        this.value = 1;
    }

    @Override
    public void clear() {
        this.value = 0;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public bit and(bit other) {

        switch (this.value) {
            case 0:
                return new bit(0);
            case 1:
                switch (other.value) {
                    case 1:
                        return new bit(1);
                    case 0:
                        return new bit(0);
                }
        }

        return null;
    }

    @Override
    public bit or(bit other) {

        switch (this.value) {
            case 1:
                return new bit(1);
            case 0:
                return new bit(other.value);
        }

        return null;
    }

    @Override
    public bit xor(bit other) {

        switch (this.value) {
            case 0:
                switch (other.value) {
                    case 0:
                        return new bit(0);
                    case 1:
                        return new bit(1);
                }
            case 1:
                return new bit(other.not().value);
        }

        return null;
    }

    @Override
    public bit not() {

        switch (this.value) {
            case 0:
                return new bit(1);
            case 1:
                return new bit(0);
        }

        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
