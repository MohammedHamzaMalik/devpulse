FROM golang:1.26-alpine AS build
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o monitor .

FROM alpine:latest
WORKDIR /app
COPY --from=build /app/monitor .
ENTRYPOINT ["./monitor"]