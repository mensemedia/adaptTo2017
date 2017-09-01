package net.mensemedia.adaptto2017.snippets;
class FactorialLoopImpl implements Factorial {
    @Override
    public long fac(final int n) {
        int out = 1;

        for (int i = n; i > 1; i--) {
            out *= i;
        }

        return out;
    }
}
